import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TestAnalizComponent } from './list/test-analiz.component';
import { TestAnalizDetailComponent } from './detail/test-analiz-detail.component';
import { TestAnalizUpdateComponent } from './update/test-analiz-update.component';
import { TestAnalizDeleteDialogComponent } from './delete/test-analiz-delete-dialog.component';
import { TestAnalizRoutingModule } from './route/test-analiz-routing.module';

@NgModule({
  imports: [SharedModule, TestAnalizRoutingModule],
  declarations: [TestAnalizComponent, TestAnalizDetailComponent, TestAnalizUpdateComponent, TestAnalizDeleteDialogComponent],
  entryComponents: [TestAnalizDeleteDialogComponent],
})
export class TestAnalizModule {}
